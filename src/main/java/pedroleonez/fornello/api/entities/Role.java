package pedroleonez.fornello.api.entities;

import jakarta.persistence.*;
import lombok.*;
import pedroleonez.fornello.api.enums.RoleName;

@Entity
@Table(name="roles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleName name;
}
