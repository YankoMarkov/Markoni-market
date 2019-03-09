package markoni.services;

import markoni.repositories.ReceiptRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ReceiptServiceImpl(ReceiptRepository receiptRepository, ModelMapper modelMapper) {
        this.receiptRepository = receiptRepository;
        this.modelMapper = modelMapper;
    }

}
