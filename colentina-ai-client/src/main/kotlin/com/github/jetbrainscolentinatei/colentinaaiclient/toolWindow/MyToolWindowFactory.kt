package com.github.jetbrainscolentinatei.colentinaaiclient.toolWindow

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import com.github.jetbrainscolentinatei.colentinaaiclient.MyBundle
import com.github.jetbrainscolentinatei.colentinaaiclient.services.MyProjectService
import com.github.jetbrainscolentinatei.colentinaaiclient.services.invokeOnEventThread
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.ui.components.JBBox
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.UI.Anchor
import com.intellij.util.ui.components.JBComponent
import javax.swing.JButton
import kotlinx.coroutines.*


class MyToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.getInstance()
        val chatErrorToolWindow = MyToolWindowError(toolWindow)
        val chatErrorContent = contentFactory.createContent(chatErrorToolWindow.getContent(), "Error", false)
        val chatConceptToolWindow = MyToolWindowConcept(toolWindow)
        val chatConceptContent = contentFactory.createContent(chatConceptToolWindow.getContent(), "Concept", false)
        val chatStyleToolWindow = MyToolWindowStyle(toolWindow)
        val chatStyleContent = contentFactory.createContent(chatStyleToolWindow.getContent(), "Style", false)
        toolWindow.contentManager.addContent(chatErrorContent)
        toolWindow.contentManager.addContent(chatConceptContent)
        toolWindow.contentManager.addContent(chatStyleContent)
    }

    override fun shouldBeAvailable(project: Project) = true

    sealed class MyToolWindowCommon(toolWindow: ToolWindow) {

        internal val service = toolWindow.project.service<MyProjectService>()
        internal val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        suspend fun requestOpenFile(): String? = service.getOpenFile()

        abstract fun getContent(): JBScrollPane
    }

    class HintTextArea : JBTextArea("", 20, 20) {
        init {
            lineWrap = true
            isEditable = false
            isVisible = true
        }
    }

    class MyToolWindowError(toolWindow: ToolWindow) : MyToolWindowCommon(toolWindow) {
        private suspend fun doRequest(code: String, error: String, task: String?): String =
            service.chatError(code, error, task)

        override fun getContent() = JBScrollPane(JBPanel<JBPanel<*>>().apply {
            val errorTextArea = JBTextField(30)
            val taskTextArea = JBTextField(30)
            val hintTextArea = HintTextArea()

            add(JBBox.createVerticalBox().apply {
                add(JBPanel<JBPanel<*>>().apply {
                    add(JBLabel("Error: "))
                    add(errorTextArea)
                })

                add(JBPanel<JBPanel<*>>().apply {
                    add(JBLabel("Task: "))
                    add(taskTextArea)
                })

                add(JBBox.createVerticalBox().apply {
                    add(JBLabel("Hint: "))
                    add(hintTextArea)
                })

                add(JButton(MyBundle.message("getHint")).apply {
                    val btn = this
                    addActionListener {
                        btn.isEnabled = false
                        coroutineScope.launch {
                            val file = requestOpenFile() ?: return@launch

                            val hint = runCatching {
                                doRequest(file, errorTextArea.text, taskTextArea.text)
                            }.onFailure {
                                thisLogger().warn(it)
                            }.getOrNull()

                            btn.isEnabled = true

                            if (hint != null) {
                                invokeOnEventThread {
                                    hintTextArea.text = hint
                                }
                            }
                        }
                    }
                })
            })
        })
    }

    class MyToolWindowStyle(toolWindow: ToolWindow) : MyToolWindowCommon(toolWindow) {
        private suspend fun doRequest(code: String, task: String?): String =
            service.chatStyle(code, task)

        override fun getContent() = JBScrollPane(JBPanel<JBPanel<*>>().apply {
            val taskTextArea = JBTextField(30)
            val hintTextArea = HintTextArea()

            add(JBBox.createVerticalBox().apply {
                add(JBPanel<JBPanel<*>>().apply {
                    add(JBLabel("Task: "))
                    add(taskTextArea)
                })

                add(JBBox.createVerticalBox().apply {
                    add(JBLabel("Hint: "))
                    add(hintTextArea)
                })

                add(JButton(MyBundle.message("getHint")).apply {
                    val btn = this
                    addActionListener {
                        btn.isEnabled = false
                        coroutineScope.launch {
                            val file = requestOpenFile() ?: return@launch

                            val hint = runCatching {
                                doRequest(file, taskTextArea.text)
                            }.onFailure {
                                thisLogger().warn(it)
                            }.getOrNull()

                            btn.isEnabled = true

                            if (hint != null) {
                                invokeOnEventThread {
                                    hintTextArea.text = hint
                                }
                            }
                        }
                    }
                })
            })
        })
    }

    class MyToolWindowConcept(toolWindow: ToolWindow) : MyToolWindowCommon(toolWindow) {
        private suspend fun doRequest(code: String?, concept: String, task: String?): String =
            service.chatConcept(code, concept, task)

        override fun getContent() = JBScrollPane(JBPanel<JBPanel<*>>().apply {
            val conceptTextArea = JBTextField(30)
            val taskTextArea = JBTextField(30)
            val hintTextArea = HintTextArea()

            add(JBBox.createVerticalBox().apply {
                add(JBPanel<JBPanel<*>>().apply {
                    add(JBLabel("Concept: "))
                    add(conceptTextArea)
                })

                add(JBPanel<JBPanel<*>>().apply {
                    add(JBLabel("Task: "))
                    add(taskTextArea)
                })

                add(JBBox.createVerticalBox().apply {
                    add(JBLabel("Hint: "))
                    add(hintTextArea)
                })

                add(JButton(MyBundle.message("getHint")).apply {
                    val btn = this
                    addActionListener {
                        btn.isEnabled = false
                        coroutineScope.launch {
                            val file = requestOpenFile()

                            val hint = runCatching {
                                doRequest(file, conceptTextArea.text, taskTextArea.text)
                            }.onFailure {
                                thisLogger().warn(it)
                            }.getOrNull()

                            btn.isEnabled = true

                            if (hint != null) {
                                invokeOnEventThread {
                                    hintTextArea.text = hint
                                }
                            }
                        }
                    }
                })
            })
        })
    }
}
