package org.yanmark.markoni.services;

import org.yanmark.markoni.domain.entities.Receipt;
import org.yanmark.markoni.domain.models.services.ReceiptServiceModel;
import org.yanmark.markoni.repositories.ReceiptRepository;
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
	
	@Override
	public ReceiptServiceModel saveReceipt(ReceiptServiceModel receiptService) {
		Receipt receipt = this.modelMapper.map(receiptService, Receipt.class);
		receipt = this.receiptRepository.saveAndFlush(receipt);
		if (receipt == null) {
			return null;
		}
		return this.modelMapper.map(receipt, ReceiptServiceModel.class);
	}
}
