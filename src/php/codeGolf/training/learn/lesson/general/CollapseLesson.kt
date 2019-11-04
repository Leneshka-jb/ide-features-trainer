package php.codeGolf.training.learn.lesson.general

import php.codeGolf.training.learn.interfaces.Module
import php.codeGolf.training.learn.lesson.kimpl.KLesson
import php.codeGolf.training.learn.lesson.kimpl.LessonContext
import php.codeGolf.training.learn.lesson.kimpl.LessonSample

class CollapseLesson(module: Module, lang: String, private val sample: LessonSample) :
    KLesson("Collapse", module, lang) {
  override val lessonContent: LessonContext.() -> Unit
    get() = {
      prepareSample(sample)

      actionTask("CollapseRegion") {
        "Sometimes you need to collapse a piece of code for better readability. Try collapsing code with ${action(it)}."
      }
      actionTask("ExpandRegion") {
        "To expand a code region, hit ${action(it)}."
      }
      actionTask("CollapseAllRegions") {
        "If you want to collapse all regions in the file, use ${action(it)}."
      }
      actionTask("ExpandAllRegions") {
        "Similarly, press ${action(it)} to expand all available regions."
      }
    }
}