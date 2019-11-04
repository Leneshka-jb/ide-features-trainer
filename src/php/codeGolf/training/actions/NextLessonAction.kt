package php.codeGolf.training.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import php.codeGolf.training.ui.LearnToolWindowFactory
import php.codeGolf.training.ui.views.LearnPanel

/**
 * Skip lesson or go to the next lesson
 *
 * Shortcuts:
 * win: alt + shift + right
 * mac: ctrl + shift + right
 * (see the plugin.xml to change shortcuts)
 */
class NextLessonAction : AnAction() {

  val LOG = Logger.getInstance(this.javaClass)

  override fun actionPerformed(e: AnActionEvent) {
    //check if the lesson view is active
    if (e.project == null) { LOG.warn("Unable to perform NextLesson action for 'null' project"); return }
    val project = e.project
    val myLearnToolWindow = LearnToolWindowFactory.learnWindowPerProject[e.project!!] ?: throw Exception("Unable to get Learn toolwindow for project (${project!!.name})")
    val view = myLearnToolWindow.scrollPane?.viewport?.view ?: throw Exception("Unable to get Learn toolwindow scrollpane or viewport (${e.project?.name})")
    //click button to skip or go to the next lesson
    (view as? LearnPanel)?.clickButton()
  }
}
