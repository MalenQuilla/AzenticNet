import httpx
from loguru import logger

SPRING_CONFIG_SERVER_URL = "http://localhost:8760"
APPLICATION_NAMES = ["ai", "common"]
PROFILE = "default"
USERNAME = "admin"
PASSWORD = "malenquilla"


def load_configs():
    auth = httpx.BasicAuth(username=USERNAME, password=PASSWORD)
    with httpx.Client(auth=auth) as client:
        configs = {}
        for APPLICATION_NAME in APPLICATION_NAMES:
            url = f"{SPRING_CONFIG_SERVER_URL}/{APPLICATION_NAME}/{PROFILE}"
            response = client.get(url=url)

            if response.status_code != 200:
                raise RuntimeError("Cannot boot server for since not all configs found!")

            logger.info(f"Got configs from {url} in Spring Config Server!")
            json = response.json().get("propertySources")[0].get("source")
            configs = {**configs, **json}

        return configs
