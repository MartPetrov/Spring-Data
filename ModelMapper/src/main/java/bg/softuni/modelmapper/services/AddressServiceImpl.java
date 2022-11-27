package bg.softuni.modelmapper.services;

import bg.softuni.modelmapper.entities.Address;
import bg.softuni.modelmapper.entities.dtos.AddressDTO;
import bg.softuni.modelmapper.repositories.AddressRepository;
import org.modelmapper.ModelMapper;


public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final ModelMapper mapper;


    public AddressServiceImpl(AddressRepository addressRepository, ModelMapper mapper) {
        this.addressRepository = addressRepository;
        this.mapper = mapper;
    }

    @Override
    public Address create(AddressDTO data) {
        ModelMapper mapper = new ModelMapper();
        Address address = mapper.map(data, Address.class);


        return this.addressRepository.save(address);
    }
}
