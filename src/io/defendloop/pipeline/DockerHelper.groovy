package io.defendloop.pipeline

import java.io.Serializable

/**
 * Utility helper for containerized Jenkins build agent orchestration.
 */
class DockerHelper implements Serializable {
    private final Object script

    DockerHelper(Object script) {
        this.script = script
    }

    void pullImageIfMissing(String imageName) {
        script.sh "docker image inspect ${imageName} > /dev/null 2>&1 || docker pull ${imageName}"
    }

    void cleanDanglingContainers(String label) {
        script.sh "docker container prune -f --filter label=${label} || true"
    }
}
