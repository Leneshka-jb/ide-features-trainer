package php.codeGolf.training.learn.lesson

import php.codeGolf.training.learn.exceptons.BadLessonException
import php.codeGolf.training.learn.exceptons.BadModuleException
import php.codeGolf.training.learn.exceptons.LessonIsOpenedException
import php.codeGolf.training.learn.interfaces.Lesson
import java.awt.FontFormatException
import java.io.IOException
import java.util.concurrent.ExecutionException

/**
 * Created by karashevich on 27/02/15.
 */
open class LessonListenerAdapter : LessonListener {

  override fun lessonStarted(lesson: Lesson) { }
  override fun lessonPassed(lesson: Lesson) { }
  override fun lessonClosed(lesson: Lesson) { }

  @Throws(BadLessonException::class, ExecutionException::class, IOException::class, FontFormatException::class, InterruptedException::class,
          BadModuleException::class, LessonIsOpenedException::class)
  override fun lessonNext(lesson: Lesson) { }

}