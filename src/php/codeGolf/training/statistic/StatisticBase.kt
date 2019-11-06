package php.codeGolf.training.statistic

import com.intellij.internal.statistic.eventLog.FeatureUsageData
import com.intellij.internal.statistic.service.fus.collectors.FUCounterUsageLogger
import com.intellij.openapi.components.ServiceManager
import php.codeGolf.training.learn.interfaces.Lesson
import php.codeGolf.training.statistic.FeatureUsageStatisticConsts.DURATION
import php.codeGolf.training.statistic.FeatureUsageStatisticConsts.LESSON_ID
import php.codeGolf.training.statistic.FeatureUsageStatisticConsts.PASSED
import php.codeGolf.training.statistic.FeatureUsageStatisticConsts.START

class StatisticBase {

    private val lessonId2StartTimestamp: MutableMap<String, Long> = LinkedHashMap()

    companion object {
        val instance: StatisticBase
            get() = ServiceManager.getService(StatisticBase::class.java)
    }

    fun onStartLesson(lesson: Lesson) {
        lessonId2StartTimestamp[lesson.id] = System.currentTimeMillis()
        logEvent(START, FeatureUsageData().addData(LESSON_ID, lesson.id))
    }

    fun onPassLesson(lesson: Lesson) {
        val timestamp = lessonId2StartTimestamp.remove(lesson.id)
        val delta = if (timestamp == null) -1 else System.currentTimeMillis() - timestamp
        logEvent(PASSED, FeatureUsageData()
                .addData(LESSON_ID, lesson.id)
                .addData(DURATION, delta))
    }
}

private fun logEvent(event: String, featureUsageData: FeatureUsageData) {
    FUCounterUsageLogger.getInstance().logEvent(GROUP_ID, event, featureUsageData)
}

//should be the same as res/META-INF/plugin.xml <statistics.counterUsagesCollector groupId="ideFeaturesTrainer" version="2"/>
private const val GROUP_ID = "php.codeGolf.ideFeaturesTrainer"

