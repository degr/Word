package org.forweb.word.service;

import org.forweb.database.AbstractService;
import org.forweb.word.dao.ModuleDao;
import org.forweb.word.entity.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModuleService extends AbstractService<Module, ModuleDao> {
    @Autowired
    private WordService wordService;

    public void delete(Iterable<Module> list) {
        for (Module module : list) {
            wordService.deleteByModuleId(module.getId());
        }
        this.dao.delete(list);
    }

    public void delete(Module module) {
        wordService.deleteByModuleId(module.getId());
        this.dao.delete(module);
    }

    public void delete(Integer id) {
        wordService.deleteByModuleId(id);
        this.dao.delete(id);
    }

    @Override
    public Module save(Module module) {
        Module item = dao.findByTitle(module.getTitle());
        if (item != null && !item.getId().equals(module.getId())) {
            throw new RuntimeException("Module with same title not found");
        }
        return dao.save(module);
    }

    @Override
    public Iterable<Module> save(Iterable<Module> list) {
        List<Module> out = new ArrayList<>();
        for (Module module : list) {
            out.add(save(module));
        }
        return out;
    }

    public Module findByTitle(String moduleTitle) {
        return dao.findByTitle(moduleTitle);
    }
}
