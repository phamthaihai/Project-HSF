package swt.he182176.hsfproject.entity;


import jakarta.persistence.*;

@Entity
@Table(name="Post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int post_id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "content")
    private String content;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @Column(name = "user_id")
    private User id;

    public Post() {
    }



}
