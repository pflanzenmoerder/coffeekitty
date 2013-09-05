package de.caffeine.kitty.service.repository.user;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.codec.binary.Base64;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import de.caffeine.kitty.entities.User;

import de.caffeine.kitty.entities.QUser;

public class UserRepositoryImpl implements UserRepositoryCustom {
	private Integer hashIterations = 10000;
	
    @PersistenceContext
    private EntityManager em;

    @Override
	public List<User> findUsersWithWarnLevelSet() {
    	JPQLQuery query = new JPAQuery(em);
        return query.from(QUser.user).where(QUser.user.warnLevel.gt(0)).list(QUser.user);
	}

	@Override
    public User findUserByUsername(String username) {
        JPQLQuery query = new JPAQuery(em);
        return query.from(QUser.user).where(QUser.user.displayName.eq(username)).singleResult(QUser.user);
    }

	@Override
	public User findUserByEmail(String email) {
	    JPQLQuery query = new JPAQuery(em);
        return query.from(QUser.user).where(QUser.user.email.eq(email)).singleResult(QUser.user);
	}
    
	public void create(User user) {
        try {
            byte[] salt = getSalt();
            user.setSalt(Base64.encodeBase64String(salt));
            user.setPassword(encryptStringWithSalt(user.getPassword(), user.getSalt()));
            em.persist(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User authenticateAndGet(String email, String password) {
        try {
            User user = findUserByEmail(email);
            if (user != null && (user.getPassword().equals(encryptStringWithSalt(password, user.getSalt()))))
                return user;
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String encryptStringWithSalt(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(Base64.decodeBase64(salt));
            byte[] input = digest.digest(password.getBytes("UTF-8"));
            for (int count = 0; count < hashIterations; count++) {
                digest.reset();
                input = digest.digest(input);
            }
            return Base64.encodeBase64String(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] getSalt() {
        try {
            SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[8];
            rnd.nextBytes(salt);
            return salt;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
