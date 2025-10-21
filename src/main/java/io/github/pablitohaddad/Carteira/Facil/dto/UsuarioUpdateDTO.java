package io.github.pablitohaddad.Carteira.Facil.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioUpdateDTO {

    // O nome é obrigatório para atualização, se for um PUT completo
    @NotBlank(message = "O nome não pode ser vazio")
    private String nome;

    // Email pode ser opcional ou exigido, dependendo da regra de negócio.
    // Vamos fazê-lo opcional para uma atualização mais flexível.
    @Email(message = "E-mail deve ser válido")
    private String email;

    // Senha é opcional, só deve ser enviada se o usuário quiser trocá-la
    @Size(min = 6, message = "A nova senha deve ter pelo menos 6 caracteres")
    private String novaSenha;
}