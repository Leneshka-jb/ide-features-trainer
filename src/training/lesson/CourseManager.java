package training.lesson;

import com.intellij.ide.scratch.ScratchFileService;
import com.intellij.ide.scratch.ScratchRootType;
import com.intellij.lang.Language;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.fileEditor.impl.EditorsSplitters;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ex.WindowManagerEx;
import com.intellij.openapi.wm.impl.IdeFrameImpl;
import com.intellij.openapi.wm.impl.IdeRootPane;
import com.intellij.refactoring.safeDelete.SafeDeleteDialog;
import com.intellij.refactoring.safeDelete.SafeDeleteProcessor;
import com.intellij.refactoring.util.CommonRefactoringUtil;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jetbrains.annotations.Nullable;
import training.editor.EduEditorProvider;
import training.lesson.dialogs.SdkProblemDialog;
import training.lesson.exceptons.BadCourseException;
import training.lesson.exceptons.BadLessonException;
import training.lesson.exceptons.InvalidSdkException;
import training.lesson.exceptons.OldJdkException;
import training.util.GenerateCourseXml;
import training.util.LearnUiUtil;
import training.util.MyClassLoader;
import training.editor.EduEditor;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * Created by karashevich on 11/03/15.
 */
public class CourseManager{

    public static final CourseManager INSTANCE = new CourseManager();
    public static final Dimension DIMENSION = new Dimension(500, 60);

    public ArrayList<Course> courses;
    private HashMap<Course, VirtualFile> mapCourseVirtualFile;


    public static CourseManager getInstance(){
        return INSTANCE;
    }

    public CourseManager() {
        //init courses; init default course by default
        courses = new ArrayList<Course>();
        mapCourseVirtualFile = new HashMap<Course, VirtualFile>();


        try {
//            final Course defaultCourse = Course.initCourse("EditorBasics.xml");
//            courses.add(defaultCourse);
            initCourses();
        } catch (BadCourseException e) {
            e.printStackTrace();
        } catch (BadLessonException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void initCourses() throws JDOMException, IOException, URISyntaxException, BadCourseException, BadLessonException {
        Element coursesRoot = Course.getRootFromPath(GenerateCourseXml.COURSE_ALLCOURSE_FILENAME);
        for (Element element : coursesRoot.getChildren()) {
            if(element.getName().equals(GenerateCourseXml.COURSE_TYPE_ATTR)) {
                String courseFilename = element.getAttribute(GenerateCourseXml.COURSE_NAME_ATTR).getValue();
                final Course course = Course.initCourse(courseFilename);
                courses.add(course);
            }
        }
    }


    @Nullable
    public Course getAnyCourse(){
        if(courses == null || courses.size() == 0) return null;
        return courses.get(0);
    }

    @Nullable
    public Course getCourseById(String id){
        if(courses == null || courses.size() == 0) return null;

        for(Course course: courses){
            if(course.getId().toUpperCase().equals(id.toUpperCase())) return course;
        }
        return null;
    }

    public void registerVirtaulFile(Course course, VirtualFile virtualFile){
        mapCourseVirtualFile.put(course, virtualFile);
    }

    public boolean isVirtualFileRegistered(VirtualFile virtualFile){
        return mapCourseVirtualFile.containsValue(virtualFile);
    }

    public void unregisterVirtaulFile(VirtualFile virtualFile){
        if(!mapCourseVirtualFile.containsValue(virtualFile)) return;
        for (Course course : mapCourseVirtualFile.keySet()) {
            if(mapCourseVirtualFile.get(course).equals(virtualFile)) {
                mapCourseVirtualFile.remove(course);
                return;
            }
        }
    }

    public void unregisterCourse(Course course){
        mapCourseVirtualFile.remove(course);
    }




    public synchronized void openLesson(final Project project, final @Nullable Lesson lesson) throws BadCourseException, BadLessonException, IOException, FontFormatException, InterruptedException, ExecutionException, LessonIsOpenedException {

        try {
            checkEnvironment(project);


            if (lesson == null) throw new BadLessonException("Cannot open \"null\" lesson");
            if (lesson.isOpen()) throw new LessonIsOpenedException(lesson.getId() + " is opened");

            //If lesson from some course
            if(lesson.getCourse() == null) return;
            VirtualFile vf = null;
            //If virtual file for this course exists;
            if (mapCourseVirtualFile.containsKey(lesson.getCourse()))
                vf = mapCourseVirtualFile.get(lesson.getCourse());
            if (vf == null || !vf.isValid()) {
                //while course info is not stored
                final String courseName = lesson.getCourse().getName();

                //find file if it is existed
                vf = ScratchFileService.getInstance().findFile(ScratchRootType.getInstance(), courseName, ScratchFileService.Option.existing_only);
                if (vf != null) {
                    FileEditorManager.getInstance(project).closeFile(vf);
                    ScratchFileService.getInstance().getScratchesMapping().setMapping(vf, Language.findLanguageByID("JAVA"));
                }


                if (vf == null || !vf.isValid()) {
                    vf = ScratchRootType.getInstance().createScratchFile(project, courseName, Language.findLanguageByID("JAVA"), "");
                    final VirtualFile finalVf = vf;
                    if (!vf.getName().equals(courseName)) {
                        ApplicationManager.getApplication().runWriteAction(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    finalVf.rename(project, courseName);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
                registerVirtaulFile(lesson.getCourse(), vf);


            }

            //open next lesson if current is passed
            lesson.addLessonListener(new LessonListenerAdapter(){
                @Override
                public void lessonNext(Lesson lesson) throws BadLessonException, ExecutionException, IOException, FontFormatException, InterruptedException, BadCourseException, LessonIsOpenedException {
                    if (lesson.getCourse() == null) return;

                    if(lesson.getCourse().hasNotPassedLesson()) {
                        Lesson nextLesson = lesson.getCourse().giveNotPassedAndNotOpenedLesson();
                        if (nextLesson == null) throw new BadLessonException("Unable to obtain not passed and not opened lessons");
                        openLesson(project, nextLesson);
                    }
                }
            });

            final String target;
            if(lesson.getTargetPath() != null) {
                InputStream is = MyClassLoader.getInstance().getResourceAsStream(lesson.getCourse().getAnswersPath() + lesson.getTargetPath());
                if (is == null) throw new IOException("Unable to get answer for \"" + lesson.getId() + "\" lesson");
                target = new Scanner(is).useDelimiter("\\Z").next();
            } else {
                target = null;
            }


            //Dispose balloon while scratch file is closing. InfoPanel still exists.
            project.getMessageBus().connect(project).subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerListener() {
                @Override
                public void fileOpened(FileEditorManager source, VirtualFile file) {

                }

                @Override
                public void fileClosed(FileEditorManager source, VirtualFile file) {
                }

                @Override
                public void selectionChanged(FileEditorManagerEvent event) {
                }
            });

            //Get EduEditor for the lesson. Select corresponding when EduEditor for this course is opened. Create a new EduEditor if no lessons for this course are opened.
            EduEditor eduEditor = getEduEditor(project, vf);
            assert eduEditor != null;
            eduEditor.selectIt(); // Select EduEditor with this lesson.

            //Process lesson
            LessonProcessor.process(lesson, eduEditor, project, eduEditor.getEditor().getDocument(), target);

        } catch (OldJdkException oldJdkException) {
            oldJdkException.printStackTrace();
        } catch (InvalidSdkException e) {
            showSdkProblemDialog(project);
        }
    }

    @Nullable
    private EduEditor getEduEditor(Project project, VirtualFile vf) {
        OpenFileDescriptor descriptor = new OpenFileDescriptor(project, vf);
        final FileEditor[] allEditors = FileEditorManager.getInstance(project).getAllEditors(vf);
        if(allEditors == null || allEditors.length == 0) {
            FileEditorManager.getInstance(project).openEditor(descriptor, true);
        } else {
            boolean editorIsFind = false;
            for (FileEditor curEditor : allEditors) {
                if(curEditor instanceof EduEditor) editorIsFind = true;
            }
            if (!editorIsFind) {
//              close other editors with this file
                FileEditorManager.getInstance(project).closeFile(vf);
                ScratchFileService.getInstance().getScratchesMapping().setMapping(vf, Language.findLanguageByID("JAVA"));
                FileEditorManager.getInstance(project).openEditor(descriptor, true);
            }
        }
        final FileEditor selectedEditor = FileEditorManager.getInstance(project).getSelectedEditor(vf);

        EduEditor eduEditor = null;
        if (selectedEditor instanceof EduEditor) eduEditor = (EduEditor) selectedEditor;
        else eduEditor = (EduEditor) (new EduEditorProvider()).createEditor(project, vf);
        return eduEditor;
    }

    public void checkEnvironment(Project project) throws OldJdkException, InvalidSdkException {

        final Sdk projectJdk = ProjectRootManager.getInstance(project).getProjectSdk();
        assert projectJdk != null;
        final SdkTypeId sdkType = projectJdk.getSdkType();
        if (sdkType instanceof JavaSdk) {
            final JavaSdkVersion version = ((JavaSdk) sdkType).getVersion(projectJdk);
            if (version != null) {
                if (!version.isAtLeast(JavaSdkVersion.JDK_1_6)) throw new OldJdkException(JavaSdkVersion.JDK_1_6);
            }
            //TODO: Replace "SDK" with "JDK"
        } else if (sdkType.getName().equals("IDEA SDK")) {
            //do nothing
        } else {
            throw new InvalidSdkException("Training plugin needs Java SDK or IDEA SDK as a Project SDK");
        }
    }

    public void showSdkProblemDialog(Project project){
        final SdkProblemDialog dialog = new SdkProblemDialog(project, null);
        dialog.show();
//        if (needConfirmation) {
//            if (!dialog.showAndGet() || exit.get()) {
//                return;
//            }
//        }

    }

}