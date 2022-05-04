package org.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@Table(name = "node")
@NoArgsConstructor
public class Node {
    @Id
    @Column(name = "node_id")
    long nodeId;

    @Column(name = "user_name")
    String userName;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id")
    private List<Tag> tags;

    double lat;

    double lon;

    public Node(generated.Node generatedNode) {
        nodeId = generatedNode.getId().longValue();
        userName = generatedNode.getUser();
        lat = generatedNode.getLat();
        lon = generatedNode.getLon();

        tags = generatedNode.getTag().stream().map(Tag::new).collect(Collectors.toList());
    }
}
