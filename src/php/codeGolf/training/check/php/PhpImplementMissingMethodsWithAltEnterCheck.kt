package php.codeGolf.training.check.php

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.jetbrains.php.lang.psi.PhpFile
import com.jetbrains.php.lang.psi.PhpPsiUtil
import php.codeGolf.training.check.Check

class PhpImplementMissingMethodsWithAltEnterCheck : Check {

    private var project: Project? = null
    private var editor: Editor? = null

    override fun set(project: Project, editor: Editor) {
        this.project = project
        this.editor = editor
    }

    override fun before() {}

    override fun check(): Boolean {
        val document = editor?.document ?: return false
        val manager = project?.let { PsiDocumentManager.getInstance(it) } ?: return false
        val file = manager.getPsiFile(document) as? PhpFile ?: return false
        val clazz = PhpPsiUtil.findClass(file) { aClass -> aClass.name == "Fixer" } ?: return false
        return clazz.methods.any { it.name == "yetToImplementFunction" }
    }

    override fun listenAllKeys(): Boolean = false
}