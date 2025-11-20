package com.advancedMobileProgramming.domain.equipment.application;

import com.advancedMobileProgramming.domain.category.entity.Category;
import com.advancedMobileProgramming.domain.category.exception.CategoryNotExistException;
import com.advancedMobileProgramming.domain.category.repository.CategoryRepository;
import com.advancedMobileProgramming.domain.equipment.converter.EquipmentConverter;
import com.advancedMobileProgramming.domain.equipment.dto.EquipmentDtos;
import com.advancedMobileProgramming.domain.equipment.entity.Equipment;
import com.advancedMobileProgramming.domain.equipment.exception.*;
import com.advancedMobileProgramming.domain.equipment.repository.EquipmentRepository;
import com.advancedMobileProgramming.domain.user.entity.User;
import com.advancedMobileProgramming.domain.user.entity.enums.Role;
import com.advancedMobileProgramming.domain.user.exception.UserNotAdmin;
import com.advancedMobileProgramming.domain.user.exception.UserNotFoundException;
import com.advancedMobileProgramming.domain.user.repository.UserRepository;
import com.advancedMobileProgramming.global.util.file.FileStorageService;
import com.advancedMobileProgramming.global.util.vertexAI.VertexAiDtos;
import com.advancedMobileProgramming.global.util.vertexAI.VertexAiService;
import com.advancedMobileProgramming.global.util.vision.VisionDtos;
import com.advancedMobileProgramming.global.util.vision.VisionProperties;
import com.advancedMobileProgramming.global.util.vision.VisionWarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipmentServiceImpl implements EquipmentService {
    private final CategoryRepository categoryRepository;
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final FileStorageService storage;
    private final VisionProperties visionProps;
    private final VisionWarehouseService visionWarehouseService;
    private final VertexAiService vertexAiService;


    private void checkAdminRole(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (user.getRole() != Role.ADMIN) throw new UserNotAdmin();
    }

    @Override
    public EquipmentDtos.EquipmentResponseDto addEquipment(Long userId, EquipmentDtos.EquipmentAddRequestDto req,
                                                           MultipartFile mainImage, List<MultipartFile> images) throws IOException {
        // 관리자인지 먼저 확인
        checkAdminRole(userId);

        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(CategoryNotExistException::new);
        if (equipmentRepository.existsEquipmentByName(req.getName())) throw new ExistedEquipmentException();
        if (equipmentRepository.existsEquipmentByVisionCode(req.getVisionCode())) throw new ExistedEquipmentException();
        if (mainImage == null) throw new UnkownMainEquipmentImage();
        if (images == null) images = List.of(); // 빈 리스트로 NPE 방어

        // 구글 비전에 상품 등록
        if ("seed".equalsIgnoreCase(visionProps.getRole())) {
            visionWarehouseService.registerEquipmentToVision(req.getVisionCode(), mainImage, images);
        }

        // 이미지 저장
        String imageUrl = storage.save(mainImage, "equipment");
        Equipment equipment = Equipment.addEquipment(category, req.getModelName(), req.getName(), req.getManufacturer(),
                req.getPurchaseYear(), req.getLocation(), req.getUse(), req.getRemainNum(), imageUrl, req.getVisionCode());

        equipmentRepository.save(equipment);
        return EquipmentConverter.toEquipmentAddResponseDto(equipment);
    }

    @Override
    public void addEquipmentImage(Long userId, String visionCode, List<MultipartFile> images) throws IOException {
        // 관리자인지 먼저 확인
        checkAdminRole(userId);

        if (!equipmentRepository.existsEquipmentByVisionCode(visionCode)) throw new NotExistedEquipmentException();
        if (images == null) images = List.of(); // 빈 리스트로 NPE 방어

        // 구글 비전에 상품 이미지 데이터 추가
        if ("seed".equalsIgnoreCase(visionProps.getRole())) {
            visionWarehouseService.appendImagesToProduct(visionCode, images);
        }
        else throw new CannotAddImageVision();
    }

    @Override
    public EquipmentDtos.EquipmentResponseDto modifyEquipment(Long userId, Long equipmentId, EquipmentDtos.EquipmentModifyRequestDto req) {
        // 관리자인지 먼저 확인
        checkAdminRole(userId);
        Equipment equipment = equipmentRepository.findById(equipmentId).orElseThrow(NotExistedEquipmentException::new);
        if (req.getModelName() != null) equipment.setModelName(req.getModelName());
        if (req.getName() != null) equipment.setName(req.getName());
        if (req.getCategoryId() != null) {
            Category category = categoryRepository.findById(req.getCategoryId()).orElseThrow(CategoryNotExistException::new);
            equipment.setCategory(category);
        }
        if (req.getManufacturer() != null) equipment.setManufacturer(req.getManufacturer());
        if (req.getPurchaseYear() != null) equipment.setPurchaseYear(req.getPurchaseYear());
        if (req.getLocation() != null) equipment.setLocation(req.getLocation());
        if (req.getUse() != null) equipment.setUse(req.getUse());
        if (req.getRemainNum() != null) equipment.setRemainNum(req.getRemainNum());
        return EquipmentConverter.toEquipmentAddResponseDto(equipment);
    }

    @Override
    public void deleteEquipment(Long userId, Long equipmentId) {
        // 관리자인지 먼저 확인
        checkAdminRole(userId);
        Equipment equipment = equipmentRepository.findById(equipmentId).orElseThrow(NotExistedEquipmentException::new);
        equipmentRepository.delete(equipment);
    }

    @Override
    public EquipmentDtos.EquipmentScanResponseDto scanWithVertexAi(Long userId, MultipartFile image) throws Exception {
        if(image == null) throw new UnkownMainEquipmentImage();
        // heic 확장자의 이미지인 경우 오류 가능성이 높아서 예외 처리
        String contentType = image.getContentType();
        if (contentType != null && contentType.toLowerCase().contains("heic")) throw new UnsupportedImageFormatException();
        VertexAiDtos.RecognitionResult json = vertexAiService.recognize(image);

        Equipment equipment = equipmentRepository.findByModelName(json.getModel())
                .orElseThrow(NotMatchingEquipmentException::new);

        if (json.getScore() < 80) throw new NotMatchingEquipmentException();

        String aiCategory = json.getCategory().trim().toLowerCase();
        String eqCategory = equipment.getCategory().getName().trim().toLowerCase();
        if (!eqCategory.equals(aiCategory)) throw new NotMatchingEquipmentException();

        String aiBran = json.getBrand().trim().toLowerCase();
        String eqBrand = equipment.getManufacturer().trim().toLowerCase();
        if (!eqBrand.equals(aiBran)) throw new NotMatchingEquipmentException();

        EquipmentDtos.EquipmentResponseDto result = EquipmentConverter.toEquipmentAddResponseDto(equipment);
        return EquipmentDtos.EquipmentScanResponseDto.builder()
                .score(json.getScore())
                .equipment(result)
                .build();
    }

//    Vision Warehouse 로 Scan 하던 코드 현재는 해당 API 사용 불가능
    @Override
    public EquipmentDtos.EquipmentScanResponseDto scanWithWarehouse(Long userId, MultipartFile image) throws IOException {
        if(image == null) throw new UnkownMainEquipmentImage();
        String contentType = image.getContentType();
        if (contentType != null && contentType.toLowerCase().contains("heic")) throw new UnsupportedImageFormatException();
        VisionDtos.ScanDto result = visionWarehouseService.scan(image);
        Equipment equipment = equipmentRepository.findByVisionCode(result.getVisionCode()).orElseThrow(NotExistedEquipmentException::new);
        EquipmentDtos.EquipmentResponseDto detail = EquipmentConverter.toEquipmentAddResponseDto(equipment);
        return EquipmentDtos.EquipmentScanResponseDto.builder()
                .score(result.getRelevance())
                .equipment(detail)
                .build();
    }

    //인기 있는 기자재 4개
    @Override
    public List<EquipmentDtos.PopularEquipmentDto> getPopularEquipments() {
        List<Equipment> equipments = equipmentRepository.findTop4ByOrderByRentalCntDesc();
        return EquipmentConverter.toPopularEquipmentDtoList(equipments);
    }

    //전체 목록 조회
    @Override
    public List<EquipmentDtos.EquipmentListDto> getAllEquipments() {
        List<Equipment> equipments = equipmentRepository.findAll();  // 필요하면 정렬 추가 가능
        return EquipmentConverter.toEquipmentListDtoList(equipments);
    }

    //기자재 검색
    @Override
    public List<EquipmentDtos.EquipmentListDto> searchEquipmentsByName(String keyword) {
        List<Equipment> equipments = equipmentRepository.findByNameContainingIgnoreCase(keyword);
        return EquipmentConverter.toEquipmentListDtoList(equipments);
    }

    //카테고리 및 대여상태 필터링
    @Override
    public List<EquipmentDtos.EquipmentListDto> filterEquipments(Long categoryId, String rentalStatus) {

        List<Equipment> equipments;

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(CategoryNotExistException::new);
            equipments = equipmentRepository.findByCategory(category);
        } else {
            equipments = equipmentRepository.findAll();
        }

        if (rentalStatus != null) {
            String upper = rentalStatus.toUpperCase();
            if (upper.equals("AVAILABLE")) {
                equipments = equipments.stream()
                        .filter(e -> e.getRemainNum() > 0)
                        .toList();
            } else if (upper.equals("UNAVAILABLE")) {
                equipments = equipments.stream()
                        .filter(e -> e.getRemainNum() == 0)
                        .toList();
            }
        }

        return EquipmentConverter.toEquipmentListDtoList(equipments);
    }

    //기자재 상세 정보 조회
    @Override
    public EquipmentDtos.EquipmentDetailDto getEquipmentDetail(Long equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(NotExistedEquipmentException::new);

        return EquipmentConverter.toEquipmentDetailDto(equipment);
    }
}