package market.services;

import market.model.dao.ClientDAO;
import market.model.persistence.Client;
import market.model.persistence.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ClientService {

    private final Logger LOG = LogManager.getLogger(ProductService.class);

    private EntityManager entityManager;

    private ClientDAO clientDAO;

    public ClientService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.clientDAO = new ClientDAO(entityManager);
    }

    public void create(Client client) {
        this.LOG.info("Preparando para a criação de um Cliente");
        if (client == null) {
            this.LOG.error("O cliente informado está nulo!");
            throw new RuntimeException("Client is null!");
        }
        try {
            this.clientDAO.create(client);
        } catch (Exception e) {
            this.LOG.error("Erro ao criar um produto: " + e.getMessage());
            throw new RuntimeException(e);
        }
        this.LOG.info("Cliente criado com sucesso!");
    }

    public void delete(Long id) {
        this.LOG.info("Preparando para buscar o Cliente");
        if (id == null) {
            this.LOG.error("O ID do Cliente informado está nulo!");
            throw new RuntimeException("The ID is Null!");
        }
        Client client = this.clientDAO.getById(id);
        validateClientIsNull(client);
        this.LOG.info("Cliente encontrado com sucesso!");

        beginTransaction();
        this.clientDAO.delete(client);
        commitAndCloseTransaction();

        this.LOG.info("Cliente deletado com sucesso!");
    }

    public void validateClientIsNull(Client client) {
        if (client == null) {
            this.LOG.error("O Cliente não Existe!");
            throw new EntityNotFoundException("Client not found!");
        }
    }

    public void update(Client newClient, Long clientId) {
        this.LOG.info("Preparando para atualizar Cliente");
        if (newClient == null || clientId == null) {
            this.LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("The parameter is null!");
        }
        Client client = this.clientDAO.getById(clientId);
        validateClientIsNull(client);
        this.LOG.info("Cliente encontrado no banco com sucesso!");

        beginTransaction();
        client.setName(newClient.getName());
        client.setCpf(newClient.getCpf());
        client.setBirthDate(newClient.getBirthDate());
        commitAndCloseTransaction();

        this.LOG.info("Cliente atualizado com sucesso!");
    }

    public List<Client> listAll() {
        this.LOG.info("Preparando para listar Clientes");
        List<Client> clients = this.clientDAO.listAll();

        if(clients == null) {
            this.LOG.info("Não foram encontrados Clientes");
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontrados " + clients.size() + " Clientes.");
        return clients;
    }

    public List<Client> listByName(String name) {
        if(name == null || name.isEmpty()) {
            this.LOG.info("O parâmetro nome está vazio!");
            throw new RuntimeException("The parameter name is null");
        }
        this.LOG.info("Preparando para buscar Clientes com o nome: " +name);
        List<Client> clients = this.clientDAO.listByName(name);

        if (clients == null) {
            this.LOG.info("Não foram encontrados Clientes");
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontrados "+ clients.size() + " Clientes com o nome "+name);
        return clients;
    }


    private void beginTransaction(){
        this.LOG.info("Abrindo Transação com o Banco de Dados");
        this.entityManager.getTransaction().begin();
    }

    private void commitAndCloseTransaction() {
        this.LOG.info("Commitando e Fechando Transação com o Banco de Dados");
        this.entityManager.getTransaction().commit();
        this.entityManager.close();
    }
}
