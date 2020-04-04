package com.stefanini.servico;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.validation.Valid;

import com.stefanini.dao.PessoaDao;
import com.stefanini.exception.NegocioException;
import com.stefanini.model.Endereco;
import com.stefanini.model.Pessoa;

/**
 * 
 * Classe de servico, as regras de negocio devem estar nessa classe
 * 
 * @author joaopedromilhome
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class PessoaServico implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PessoaDao dao;

	@Inject
	private PessoaPerfilServico pessoaPerfilServico;
	
	@Inject
	private EnderecoServico enderecoServico;

	/**
	 * Salvar os dados de uma Pessoa
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Pessoa salvar(@Valid Pessoa pessoa) {
	
	
	List<Endereco> enderecos = new ArrayList<>();
	for (Endereco enderecoDaPessoa : pessoa.getEnderecos()) {
		
		enderecos.add(enderecoDaPessoa);
	}
	
	pessoa.getEnderecos().clear();
	
	if(pessoa.getFotoPerfil() != null) {
		pessoa.setFotoPerfil(decodeToImage(pessoa.getFotoPerfil())) ;	
	}
	
	Pessoa pessoaSalva = dao.salvar(pessoa);
	
	
	for (Endereco enderecoSalvo : enderecos) {

	enderecoSalvo.setIdPessoa(pessoaSalva.getId()); 
	
	enderecoServico.salvar(enderecoSalvo);
	
	}

		return pessoaSalva;
	}
	/**
	 * Validando se existe pessoa com email
	 */
	public Boolean validarPessoa(@Valid Pessoa pessoa){
		if(pessoa.getId() != null){
			Optional<Pessoa> encontrar = dao.encontrar(pessoa.getId());
			if(encontrar.get().getEmail().equals(pessoa.getEmail())){
				return Boolean.TRUE;
			}
		}
		Optional<Pessoa> pessoa1 = dao.buscarPessoaPorEmail(pessoa.getEmail());
		return pessoa1.isEmpty();
	}

	/**
	 * Atualizar o dados de uma pessoa
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Pessoa atualizar(@Valid Pessoa pessoa) {
		pessoa.setFotoPerfil(decodeToImage(pessoa.getFotoPerfil())) ;
		return dao.atualizar(pessoa);
	}

	/**
	 * Remover uma pessoa pelo id
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void remover(@Valid Long id) throws NegocioException {
		if(pessoaPerfilServico.buscarPessoaPerfil(id,null).count() != 0 || pessoaPerfilServico.buscarPessoaPerfil(id,null).count() == 0){
			dao.remover(id);
			return;
		}
		throw new NegocioException("Não foi possivel remover a pessoa");
	}

	/**
	 * Buscar uma lista de Pessoa
	 */
	public Optional<List<Pessoa>> getList() {
		return dao.getList();
	}

	
	public Optional<List<Pessoa>> obterFullPessoa() {
		return dao.getFullPessoa();
	}

	public String decodeToImage(String imagem ) {
		imagem = imagem.split(",")[1];
				
		String url = "C:\\Users\\heric\\Desktop\\Hackathon\\Hackathon-Stefanini-Api\\src\\imagens";
		String url2 = "\\imagem"+ Math.random() + ".jpg";
		
        BufferedImage image = null;
        byte[] imageByte;
        try {
     
        	imageByte = Base64.getDecoder().decode(imagem.getBytes());
            
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
           bis.close();
           ImageIO.write((RenderedImage)image, "jpg", new File(url+url2));
       } catch (Exception e) {
           e.printStackTrace();
        }
        return url + url2 ;
    }
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public FileInputStream urlImg (String localImg){

		String local = "C:\\Users\\heric\\Desktop\\Hackathon\\Hackathon-Stefanini-Api\\src\\imagens"+localImg;

		FileInputStream file = null;

		try{
		   file = new FileInputStream(local);
		}catch (FileNotFoundException e){
		e.printStackTrace();}

		return file;
		}

	
	
	
	
	/**
	 * Buscar uma Pessoa pelo ID
	 */
	public Optional<Pessoa> encontrar(Long id) {
		
		
		Optional<Pessoa> pessoa = dao.encontrar(id);

		if (pessoa.get().getFotoPerfil() != null){

		String urlPath = "http://localhost:8080/treinamento/api/pessoas/imagem/imagem0.";
		String local = pessoa.get().getFotoPerfil();

		String[] cocatenar = local.split(Pattern.quote("."));

		pessoa.get().setFotoPerfil(urlPath + cocatenar[1] + "." + cocatenar[2]);

		}

		return pessoa;
		}
		
		
		
		
	

}