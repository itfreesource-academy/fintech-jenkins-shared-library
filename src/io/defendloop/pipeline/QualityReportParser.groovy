package io.defendloop.pipeline

import java.io.Serializable

/**
 * Parses and summarizes test execution metrics for console logging.
 */
class QualityReportParser implements Serializable {
    private final Object script

    QualityReportParser(Object script) {
        this.script = script
    }

    void printBanner(String suiteName, String status, int totalTests, int failures) {
        script.echo """
        ===============================================================
        FINTECH QUALITY GATE REPORT: ${suiteName.toUpperCase()}
        ---------------------------------------------------------------
        Status:        ${status}
        Total Tests:   ${totalTests}
        Failures:      ${failures}
        Success Rate:  ${totalTests > 0 ? ((totalTests - failures) / totalTests * 100) : 0}%
        ===============================================================
        """
    }
}
