
/**
 *
 * @author Adm
 */
import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProdutosDAO {

    Connection conn;
    PreparedStatement prep;
    ResultSet resultset;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();

    public void ProdutosDAO() {
        this.conn = new conectaDAO().connectDB();
    }

    public boolean cadastrarProduto(ProdutosDTO produto) {
        String sql = "INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)";

        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, produto.getNome());
            statement.setInt(2, produto.getValor());
            statement.setString(3, produto.getStatus());
            statement.executeUpdate();
            return true;
        } catch (SQLException err) {
            return false;
        }

    }

    public ArrayList<ProdutosDTO> listarProdutos() {
        String sql = "SELECT id, nome, valor, status FROM produtos";
        ArrayList<ProdutosDTO> listaProdutos = new ArrayList<>();

        try (PreparedStatement statement = conn.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(resultSet.getInt("id"));
                produto.setNome(resultSet.getString("nome"));
                produto.setValor(resultSet.getInt("valor"));
                produto.setStatus(resultSet.getString("status"));

                listaProdutos.add(produto);
            }

        } catch (SQLException err) {
            err.printStackTrace(); // Use logging in production code
        }

        return listaProdutos;
    }
    
    public void venderProduto(int idProduto) {
        String sql = "UPDATE produtos SET status = 'Vendido' WHERE id = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idProduto);

            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            System.out.println("Erro ao vender produto: " + e.getMessage());
        }
    }
    
    public ArrayList<ProdutosDTO> listarProdutosVendidos() {
        ArrayList<ProdutosDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE status = 'Vendido'";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setValor(rs.getInt("valor"));
                produto.setStatus(rs.getString("status"));
                
                lista.add(produto);
            }

            rs.close();
            pstmt.close();
        } catch (Exception e) {
            System.out.println("Erro ao listar produtos vendidos: " + e.getMessage());
        }

        return lista;
    }
}
