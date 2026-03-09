package swt.he182176.hsfproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swt.he182176.hsfproject.dto.SettingDTO;
import swt.he182176.hsfproject.entity.Setting;
import swt.he182176.hsfproject.repository.SettingRepository;

import java.util.List;

@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepository;

    public List<Setting> getAllTypes() {
        return settingRepository.findByTypeIsNullAndStatusTrueOrderByNameAsc();
    }

    public List<Setting> search(Integer typeId, Boolean status, String keyword, String sortField, String sortDir) {
        String safeSortField = switch (sortField) {
            case "name", "priority", "status", "value" -> sortField;
            default -> "id";
        };

        Sort sort = "desc".equalsIgnoreCase(sortDir)
                ? Sort.by(safeSortField).descending()
                : Sort.by(safeSortField).ascending();

        String kw = (keyword == null || keyword.trim().isEmpty()) ? null : keyword.trim();

        return settingRepository.search(typeId, status, kw, sort);
    }

    public SettingDTO getDTOById(Integer id) {
        Setting setting = settingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Setting not found"));

        SettingDTO dto = new SettingDTO();
        dto.setId(setting.getId());
        dto.setName(setting.getName());
        dto.setTypeId(setting.getType() != null ? setting.getType().getId() : null);
        dto.setValue(setting.getValue());
        dto.setPriority(setting.getPriority());
        dto.setStatus(setting.getStatus());
        dto.setDescription(setting.getDescription());
        return dto;
    }

    @Transactional
    public void save(SettingDTO dto) {
        String name = dto.getName() != null ? dto.getName().trim() : null;

        if (settingRepository.existsDuplicateName(name, dto.getTypeId(), dto.getId())) {
            throw new RuntimeException("Setting name must be unique within selected type");
        }

        Setting type = settingRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new RuntimeException("Type not found"));

        if (type.getType() != null) {
            throw new RuntimeException("Type must be a root setting");
        }

        Setting setting;
        if (dto.getId() == null) {
            setting = new Setting();
        } else {
            setting = settingRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Setting not found"));
        }

        setting.setName(name);
        setting.setType(type);
        setting.setValue(dto.getValue() != null ? dto.getValue().trim() : null);
        setting.setPriority(dto.getPriority());
        setting.setStatus(dto.getStatus());
        setting.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : null);

        settingRepository.save(setting);
    }

    @Transactional
    public void toggleStatus(Integer id) {
        Setting setting = settingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Setting not found"));

        setting.setStatus(!Boolean.TRUE.equals(setting.getStatus()));
        settingRepository.save(setting);
    }
}