package malenquilla.cds.authentication.object_mappers.dto_mappers;

import lombok.RequiredArgsConstructor;
import malenquilla.cds.authentication.models.RoleModel;
import malenquilla.cds.authentication.dtos.RoleDTO;
import org.modelmapper.ModelMapper;

@RequiredArgsConstructor
public class RoleMapper {
    private final ModelMapper modelMapper;

    public RoleDTO toDTO(RoleModel roleModel) {
        return this.modelMapper.map(roleModel, RoleDTO.class);
    }

    public RoleModel toModel(RoleDTO roleDTO) {
        return this.modelMapper.map(roleDTO, RoleModel.class);
    }
}
