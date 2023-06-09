package br.travelexpense.controller;

import br.travelexpense.model.Despesa;
import br.travelexpense.model.Viagem;
import br.travelexpense.repository.DespesaRepository;
import br.travelexpense.repository.ViagemRepository;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("despesa/")
public class DespesaController {

    private DespesaRepository despesaRepository;

    private ViagemRepository viagemRepository;
    private EntityManager entityManager;

    public DespesaController(DespesaRepository despesaRepository, ViagemRepository viagemRepository, EntityManager entityManager) {
        this.despesaRepository = despesaRepository;
        this.viagemRepository = viagemRepository;
        this.entityManager = entityManager;
    }

    @PostMapping("add/")
    public Despesa add(@RequestBody Despesa despesa){
       return despesaRepository.save(despesa);
    }

    @PostMapping("add/viagem/{id}")
    public Despesa addById(@RequestBody Despesa despesa, @PathVariable Long id) {
        Viagem viagem;
        viagem = viagemRepository.getReferenceById(id);

        List<Despesa> despesaList = viagem.getDespesas();
        despesaList.add(despesa);

        viagem.setDespesas(despesaList);

        viagemRepository.save(viagem);

        return despesa;
    }


    @GetMapping("get/{id}")
    public Despesa get(@PathVariable Long id){
        return despesaRepository.getReferenceById(id);
    }

    @GetMapping("/list")
    public List<Despesa> list() {
        return despesaRepository.findAll();
    }



    @GetMapping("/get-by-funcionario/{id}")
    public List<Despesa> listByFuncionario(@PathVariable Long id) {

        if(id != null){
            return despesaRepository.findAll().stream().filter(d -> d.getViagem().temFuncionario(id)).toList();
        }

        return despesaRepository.findAll();
    }


    @GetMapping("/get-by-viagem/{id}")
    public List<Despesa> listByViagem(@PathVariable Long id) {

        if(id != null){

            entityManager.unwrap(Session.class)
                    .enableFilter("viagem")
                    .setParameter("viagemId", id);

            return despesaRepository.findAll();
        }

        return despesaRepository.findAll();
    }




    @DeleteMapping("/delete/{id}")
    void delete(@PathVariable Long id) {
        despesaRepository.deleteById(id);
    }

}
