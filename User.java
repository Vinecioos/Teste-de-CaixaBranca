package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class User {

    /*
     * CORREÇÃO:
     * Antes as credenciais estavam diretamente na URL de conexão:
     * jdbc:mysql://127.0.0.1/test?user=lopes&password=123
     *
     * Agora elas foram separadas em constantes para melhorar
     * a organização e facilitar futuras alterações.
     */
    private static final String URL =
            "jdbc:mysql://127.0.0.1/test";

    private static final String USER =
            "lopes";

    private static final String PASSWORD =
            "123";

    /*
     * CORREÇÃO:
     * O método continua responsável pela conexão,
     * porém agora retorna diretamente a conexão criada.
     *
     * Em versões atuais do JDBC não é necessário utilizar:
     * Class.forName(...);
     */
    public Connection conectarBD() throws Exception {

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }

    /*
     * CORREÇÃO:
     * Foram removidas as variáveis globais:
     *
     * public String nome = "";
     * public boolean result = false;
     *
     * Elas eram desnecessárias e poderiam causar problemas
     * caso vários usuários utilizassem o sistema simultaneamente.
     */

    public boolean verificarUsuario(
            String login,
            String senha) {

        /*
         * CORREÇÃO:
         * Antes a consulta era montada por concatenação:
         *
         * sql += "where login = '" + login + "'";
         * sql += "and senha = '" + senha + "'";
         *
         * Isso permitia SQL Injection.
         *
         * Agora utilizamos PreparedStatement.
         */
        String sql =
                "SELECT nome FROM usuarios " +
                "WHERE login = ? AND senha = ?";

        /*
         * CORREÇÃO:
         * Antes Connection, Statement e ResultSet
         * não eram fechados após o uso.
         *
         * Agora foi utilizado try-with-resources,
         * garantindo o fechamento automático.
         */
        try (
                Connection conn = conectarBD();
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            /*
             * CORREÇÃO:
             * Define os parâmetros de forma segura,
             * impedindo SQL Injection.
             */
            ps.setString(1, login);
            ps.setString(2, senha);

            ResultSet rs = ps.executeQuery();

            /*
             * Se encontrar um registro,
             * o usuário existe.
             */
            return rs.next();

        } catch (Exception e) {

            /*
             * CORREÇÃO:
             * O código original possuía:
             *
             * catch(Exception e){}
             *
             * Isso ocultava qualquer erro ocorrido.
             *
             * Agora o erro é exibido para facilitar
             * a manutenção e depuração.
             */
            e.printStackTrace();

            return false;
        }
    }
}