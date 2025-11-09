package co.edu.uco.easy.victusresidencias.victus_api.service;

import co.edu.uco.easy.victusresidencias.victus_api.entity.AdministratorEntity;
import co.edu.uco.easy.victusresidencias.victus_api.repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdministratorService {

    @Autowired
    private AdministratorRepository adminRepository;

    public List<AdministratorEntity> findAll() {
        return adminRepository.findAll();
    }

    public Optional<AdministratorEntity> findById(UUID id) {
        return adminRepository.findById(id);
    }

    public AdministratorEntity save(AdministratorEntity admin) {
        return adminRepository.save(admin);
    }

    public boolean existsByName(String name) {
        return adminRepository.existsByName(name);
    }

    public void deleteById(UUID id) {
        adminRepository.deleteById(id);
    }
}