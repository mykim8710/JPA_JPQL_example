package hello.jpa.jpql.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
public class Team {
    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    // 양방향 매핑 <-> TEAM
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();
}
