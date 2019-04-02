package markoni.services;

import markoni.repositories.CommentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
	
	private final CommentRepository commentRepository;
	private final ModelMapper modelMapper;
	
	@Autowired
	public CommentServiceImpl(CommentRepository commentRepository, ModelMapper modelMapper) {
		this.commentRepository = commentRepository;
		this.modelMapper = modelMapper;
	}
}
