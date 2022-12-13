package hello.jpa.jpql.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter @Setter
@Entity
@NamedQuery(
        name = "Member.findByUsername", // Entity명.name
        query = "select m from Member m where m.username = :username"
)
public class Member { // N
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "NAME")
    private String username;
    private int age;

    @Enumerated(EnumType.STRING)
    private MemberType type;

    @ManyToOne(fetch = FetchType.LAZY) // 항상 지연로딩으로 해야함 !!!!
    @JoinColumn(name = "TEAM_ID")
    private Team team; // 1

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                '}';
    }

    // 연관관계 편의 메서드
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
