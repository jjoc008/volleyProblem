package co.edu.udistrital.volley.resolver;

import co.edu.udistrital.volley.domain.MemberCard;
import co.edu.udistrital.volley.repository.MemberCardRepository;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryMemberCard implements GraphQLQueryResolver {

    @Autowired
    private MemberCardRepository memberCardRepository;

    public List<MemberCard> getAllMemberCards() {
        return this.memberCardRepository.findAll();
    }

    public MemberCard getMemberCardById(Long id) {
        return this.memberCardRepository.findById(id).get();
    }
}
