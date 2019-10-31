package training.lang

import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import training.project.ProjectUtils

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

    override fun checkSdk(sdk: Sdk?, project: Project) {}

    override fun applyToProjectAfterConfigure(): (Project) -> Unit = {}

    override fun createProject(projectName: String, projectToClose: Project?): Project? {
        return ProjectUtils.importOrOpenProject("/learnProjects/php/LearnProject", "LearnProject")
    }
}