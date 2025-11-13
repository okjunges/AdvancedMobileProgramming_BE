package com.advancedMobileProgramming.domain.equipment.application;

import com.advancedMobileProgramming.domain.category.entity.Category;
import com.advancedMobileProgramming.domain.category.exception.CategoryNotExistException;
import com.advancedMobileProgramming.domain.category.repository.CategoryRepository;
import com.advancedMobileProgramming.domain.equipment.converter.EquipmentConverter;
import com.advancedMobileProgramming.domain.equipment.dto.EquipmentDtos;
import com.advancedMobileProgramming.domain.equipment.entity.Equipment;
import com.advancedMobileProgramming.domain.equipment.exception.CannotAddImageVision;
import com.advancedMobileProgramming.domain.equipment.exception.ExistedEquipmentException;
import com.advancedMobileProgramming.domain.equipment.exception.NotExistedEquipmentException;
import com.advancedMobileProgramming.domain.equipment.exception.UnkownMainEquipmentImage;
import com.advancedMobileProgramming.domain.equipment.repository.EquipmentRepository;
import com.advancedMobileProgramming.domain.user.entity.User;
import com.advancedMobileProgramming.domain.user.entity.enums.Role;
import com.advancedMobileProgramming.domain.user.exception.UserNotAdmin;
import com.advancedMobileProgramming.domain.user.exception.UserNotFoundException;
import com.advancedMobileProgramming.domain.user.repository.UserRepository;
import com.advancedMobileProgramming.global.util.file.FileStorageService;
import com.advancedMobileProgramming.global.util.vision.VisionProperties;
import com.advancedMobileProgramming.global.util.vision.VisionWarehouseService;
import lombok.RequiredArgsConstructor;
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
}