package security;

import java.security.Key;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

	// precisamos de uma chave secreta, há uma chave privada e outra publica
	// a chave foi criada no site https://www.keygen.io/ - SHA 256-bit Key

	public static final String SECRET = "3c2ab0306b9e421e54f9f9f8626e144cf695a99e6833f9645877edc93209ff96";

	// metodos padronizados(auxiliares), podem ser ajustados de acordo com cada
	// projeto

	private Key getSignKey() { // método que gera assinatura de login, que decodifica a secreta e retorna a
								// chave de conexão
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	private Claims extractAllClaims(String token) { // método retorna em formato Json com o Jwt dentro do corpo, usa a
													// chave q geramos anteriormente
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) { // método que retorna o token, e uma
																					// função interna do Jwt que mapeia
																					// o Json e retorna os dados
																					// necessários para acesso
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String extractUsername(String token) { // met. trazer o token
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) { // met. trás a expiração/tempo de validade do token
		return (Date) extractClaim(token, Claims::getExpiration);
	}

	private Boolean isTokenExpired(String token) { // met. verifica se o mesmo está expirado ou não
		return extractExpiration(token).before(new Date(0));
	}

	public Boolean validateToken(String token, UserDetails userDetails) { // met. p/ ver se o token tem data válida e se
																			// pertence ao usuário
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	// métodos responsáveis por gerar e criar um token

	private String createToken(Map<String, Object> claims, String userName) { // met. de criação do token
		return Jwts.builder().setClaims(claims).setSubject(userName).setIssuedAt(new Date(System.currentTimeMillis())) // data
																														// completa
																														// de
																														// qnd
																														// o
																														// token
																														// foi
																														// gerado
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // data que digo quando o token
																						// foi gerado
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact(); // assinatura do token

		// *1000 = 1s / 60.000ms = 1min / 3.6000.000ms = 60min ou 1 hora */ - cálculo de
		// duração/validade do token em uma hora
	}

	public String generateToken(String userName) { // met. de geração do token que retorna o acima, com base nas infos
													// recebidas
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userName);
	}

}
