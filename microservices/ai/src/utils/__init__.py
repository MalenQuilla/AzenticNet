from src.utils.config_utils import load_configs

configs_data = None
if not configs_data:
    configs_data = load_configs()