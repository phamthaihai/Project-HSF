package swt.he182176.hsfproject.service;

import swt.he182176.hsfproject.dto.LessonViewerDTO;
import swt.he182176.hsfproject.entity.User;

public interface LessonService {

    LessonViewerDTO getLessonViewer(User loginUser, Integer courseId, Integer lessonId);
}
