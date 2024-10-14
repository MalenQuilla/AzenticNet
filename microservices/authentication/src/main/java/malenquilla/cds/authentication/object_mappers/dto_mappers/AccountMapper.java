package malenquilla.cds.authentication.object_mappers.dto_mappers;

import lombok.RequiredArgsConstructor;
import malenquilla.cds.authentication.models.AccountModel;
import malenquilla.cds.authentication.dtos.AccountDTO;
import org.modelmapper.ModelMapper;

@RequiredArgsConstructor
public class AccountMapper {
    private final ModelMapper modelMapper;

    public AccountDTO toDto(AccountModel accountModel) {
        return this.modelMapper.map(accountModel, AccountDTO.class);
    }

    public AccountModel toModel(AccountDTO accountDTO) {
        return this.modelMapper.map(accountDTO, AccountModel.class);
    }
}
