package malenquilla.cds.authentication.dto_mappers;

import malenquilla.cds.authentication.models.AccountModel;
import malenquilla.cds.common.dtos.authentication.AccountDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public AccountMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AccountDTO toDto(AccountModel accountModel) {
        return this.modelMapper.map(accountModel, AccountDTO.class);
    }

    public AccountModel toModel(AccountDTO accountDTO) {
        return this.modelMapper.map(accountDTO, AccountModel.class);
    }
}
