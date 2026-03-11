package swt.he182176.hsfproject.entity;

import jakarta.persistence.*;

@Entity
@Table(name="lesson")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lesson_id;

    @Column(name="description")
    private String description;

    @Column(name="tiltle")
    private String tiltle;

}
