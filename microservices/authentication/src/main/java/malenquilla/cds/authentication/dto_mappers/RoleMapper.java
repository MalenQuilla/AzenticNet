package malenquilla.cds.authentication.dto_mappers;

import malenquilla.cds.authentication.models.RoleModel;
import malenquilla.cds.common.dtos.authentication.RoleDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public RoleMapper(
            ModelMapper modelMapper
    ) {
        this.modelMapper = modelMapper;
    }

    public RoleDTO toDTO(RoleModel roleModel) {
        return this.modelMapper.map(roleModel, RoleDTO.class);
    }

    public RoleModel toModel(RoleDTO roleDTO) {
        return this.modelMapper.map(roleDTO, RoleModel.class);
    }
}
