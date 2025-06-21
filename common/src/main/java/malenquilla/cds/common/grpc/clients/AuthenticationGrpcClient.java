package malenquilla.cds.common.grpc.clients;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import malenquilla.cds.common.configs.CommonValuesConfig;
import malenquilla.cds.common.exceptions.UnauthorizedException;
import malenquilla.cds.common.security.AuthoritiesAuthentication;
import malenquilla.cds.grpc.proto.AuthoritiesDetailsGrpc;
import malenquilla.cds.grpc.proto.authentication.AuthenticationControllerGrpc;
import malenquilla.cds.grpc.proto.authentication.VerifyAuthenticationRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AuthenticationGrpcClient extends AbstractGrpcClient {
    private final CommonValuesConfig commonValuesConfig;

    AuthenticationControllerGrpc.AuthenticationControllerBlockingStub blockingStub;

    @Override
    public void initStub() {
        this.blockingStub = AuthenticationControllerGrpc.newBlockingStub(
            this.initChanel(this.commonValuesConfig.getGrpcAuthHost(), this.commonValuesConfig.getGrpcAuthPort())
                .getChannel());
    }

    public AuthoritiesAuthentication verifyAuthentication(String accessToken) throws StatusRuntimeException {
        if (accessToken == null || accessToken.isEmpty()) throw new UnauthorizedException();

        VerifyAuthenticationRequest request = VerifyAuthenticationRequest.newBuilder()
                                                                         .setAccessToken(accessToken)
                                                                         .build();
        AuthoritiesDetailsGrpc authoritiesDetails = this.blockingStub.verifyAuthentication(request);

        return new AuthoritiesAuthentication(
            authoritiesDetails.getUserId(), authoritiesDetails.getAuthoritiesList()
                                                              .stream()
                                                              .map(SimpleGrantedAuthority::new)
                                                              .collect(Collectors.toSet())
        );
    }
}
