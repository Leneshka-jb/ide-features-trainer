package php.codeGolf.training.learn.lesson.listeners

import com.intellij.openapi.project.Project
import php.codeGolf.training.learn.interfaces.Lesson
import php.codeGolf.training.learn.lesson.LessonListenerAdapter
import php.codeGolf.training.statistic.StatisticBase

class StatisticLessonListener(val project: Project) : LessonListenerAdapter() {

  override fun lessonStarted(lesson: Lesson) {
    StatisticBase.instance.onStartLesson(lesson)
  }

  override fun lessonPassed(lesson: Lesson) {
    StatisticBase.instance.onPassLesson(lesson)
  }
}