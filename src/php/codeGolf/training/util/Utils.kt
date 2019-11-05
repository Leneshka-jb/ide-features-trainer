package php.codeGolf.training.util

import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.intellij.ide.DataManager
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManager
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.lang.Language
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.util.registry.Registry
import com.intellij.util.ui.UIUtil
import php.codeGolf.training.learn.CourseManager
import java.awt.Point
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.swing.Icon


fun createNamedSingleThreadExecutor(name: String): ExecutorService =
    Executors.newSingleThreadExecutor(ThreadFactoryBuilder().setNameFormat(name).build())


fun findLanguageByID(id: String?): Language? {
  val effectiveId = if (id!!.toLowerCase() == "cpp") {
    "ObjectiveC"
  } else {
    id
  }
  val languageByID = Language.findLanguageByID(effectiveId)
  return languageByID
}

fun getCurrentProject(): Project? {
  return CommonDataKeys.PROJECT.getData(DataManager.getInstance().dataContext)
}

fun editorPointForBalloon(myEditor: Editor): Point {
  val offset = myEditor.caretModel.currentCaret.offset
  val position = myEditor.offsetToVisualPosition(offset)
  val point = myEditor.visualPositionToXY(position)
  return point
}
fun createBalloon(text: String): Balloon = createBalloon(text, 3000)
fun createBalloon(text: String, delay: Long): Balloon =
    JBPopupFactory.getInstance()
        .createHtmlTextBalloonBuilder(text, null, UIUtil.getToolTipBackground(), null)
        .setHideOnClickOutside(true)
        .setCloseButtonEnabled(true)
        .setHideOnKeyOutside(true)
        .setAnimationCycle(0)
        .setFadeoutTime(delay)
        .createBalloon()

val featureTrainerMode: TrainingMode
  get() =
    @Suppress("InvalidBundleOrProperty")
    when (Registry.stringValue("ide.features.trainer.mode")) {
      "public-demo" -> TrainingMode.DEMO
      "development" -> TrainingMode.DEVELOPMENT
      "" -> TrainingMode.NORMAL
      else -> TrainingMode.NORMAL
    }

const val trainerPluginConfigName : String = "ide-features-trainer.xml"

val featureTrainerVersion: String by lazy {
  val featureTrainerPluginId = PluginManagerCore.getPluginByClassName(CourseManager::class.java.name)
  getPlugin(featureTrainerPluginId)?.version ?: "UNKNOWN"
}

internal fun getPlugin(id: PluginId?): IdeaPluginDescriptor? {
  if (id != null) {
    for (plugin in PluginManager.getPlugins()) {
      if (id === plugin.getPluginId()) {
        return plugin
      }
    }
  }
  return null
}

val isFeatureTrainerSnapshot: Boolean by lazy {
  featureTrainerVersion.contains("SNAPSHOT")
}

fun createAnAction(icon: Icon, action: (AnActionEvent) -> Unit): AnAction {
  return object: AnAction(icon) {
    override fun isDumbAware() = true
    override fun actionPerformed(e: AnActionEvent) { action(e) }
  }
}