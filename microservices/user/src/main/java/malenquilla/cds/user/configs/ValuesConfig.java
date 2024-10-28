package malenquilla.cds.user.configs;

import malenquilla.cds.common.configs.CommonValuesConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CommonValuesConfig.class)
public class ValuesConfig {
}
