package php.codeGolf.training.lang

import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.jetbrains.php.config.PhpLanguageLevel
import com.jetbrains.php.config.PhpProjectConfigurationFacade
import php.codeGolf.training.project.ProjectUtils

class PhpStormSupport : AbstractLangSupport() {

    companion object {
        @JvmStatic
        val lang: String = "php"
    }

    override val filename: String
        get() = "learning.php"

    override fun importLearnProject(): Project? {
        TODO("not implemented")
    }

    override val primaryLanguage: String
        get() = lang

    override fun checkSdk(sdk: Sdk?, project: Project) {
        PhpProjectConfigurationFacade.getInstance(project).languageLevel = PhpLanguageLevel.PHP730
    }

    override fun applyToProjectAfterConfigure(): (Project) -> Unit = {}

    override fun createProject(projectName: String, projectToClose: Project?): Project? {
        return ProjectUtils.importOrOpenProject("/learnProjects/php/LearnProject", "LearnProject")
    }
}