package no.cardwallet.card.AppUser;

import org.springframework.data.repository.CrudRepository;


public interface AppUserRepository extends CrudRepository<AppUser, Long> {

    AppUser findByEmail(String email);

    void deleteAppUserByEmail(String email);

    AppUser findAppUserByEmail (String email);
}
