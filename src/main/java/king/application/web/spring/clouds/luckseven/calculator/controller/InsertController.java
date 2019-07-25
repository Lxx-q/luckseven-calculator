/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package king.application.web.spring.clouds.luckseven.calculator.controller;

import com.king.wind.spring.boot.jdbc.function.JdbcFunction;
import java.util.Date;
import king.application.web.spring.clouds.luckseven.calculator.model.bean.common.Login;
import king.application.web.spring.clouds.luckseven.calculator.model.bean.common.User;
import king.application.web.spring.clouds.luckseven.calculator.model.bean.magazine.Favorites;
import king.application.web.spring.clouds.luckseven.calculator.model.bean.magazine.Peridocial;
import king.application.web.spring.clouds.luckseven.calculator.model.repository.FavoritesRepository;
import king.application.web.spring.clouds.luckseven.calculator.model.repository.LoginRepository;
import king.application.web.spring.clouds.luckseven.calculator.model.repository.PeridocialRepository;
import king.application.web.spring.clouds.luckseven.calculator.model.repository.UserRepository;
import king.application.web.spring.clouds.luckseven.calculator.service.IdService;
import king.application.web.spring.clouds.luckseven.calculator.service.JdbcFunctionService;
import king.application.web.spring.clouds.luckseven.calculator.service.ModelService;
import king.application.web.spring.clouds.luckseven.calculator.service.jpa.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author king
 */
@RequestMapping("insert")
@RestController
public class InsertController {

    @Autowired
    private IdService id;

    @Autowired
    private ModelService model;

    @Autowired
    private LoginRepository login;

    @Autowired
    private UserRepository user;

    @Autowired
    private PeridocialRepository peridocial;

    @Autowired
    private FavoritesRepository favorites;

    @Autowired
    private JdbcFunctionService jdbcfunction;

    @Autowired
    private RepositoryService repository;

    @RequestMapping("peridocial")
    public Object insert_peridocial(Peridocial peridocial) {
        String id = this.id.create(peridocial);
        peridocial.setId(id);

        this.model.doJdbcFunction(this.peridocial, this.jdbcfunction.insert(peridocial));
        return this.model.toString();

    }

    @RequestMapping("favorites")
    public Object insert_favorites(Favorites favorites) {
        //进行 输入 时间 ， 但是这可以 交给 客户端 来实行 ， 但是 也可以 不需要
        favorites.setTime(new Date());

        this.model.doJdbcFunction(this.favorites, this.jdbcfunction.insert(favorites));
        return this.toString();
    }

    //根据 相对应的 请求 ， 来进行 相对应的 信息 保存  , 
    //我们 需要 在 这里 获取 新对应的 注册 信息
    @RequestMapping("register")
    public Object register(Login login, User user) {

        //暂时 不需要 如此
        //String _id = this.id.create(this);
        String _id = new StringBuilder().append("u").append(((int) (Math.random() * 1000))).toString();
        //我们使用 相对应的 方法 ， 来 进行 相对应的 注册 信息

        //然后 我们分别的 将 相对应的 信息 保存 至
        login.setId(_id);
        user.setId(_id);

        //之后 我们 便将 相对应的 请求 进行 保存 ， 保存 至 相对应的 信息 列表 之中
        //目前，我们 暂时做 相对应的 两次 访问的 请求 ，但是 之后会改
        this.repository.doJdbcFunction(new JdbcFunction<RepositoryService, Boolean>() {
            @Override
            public Boolean doFunction(RepositoryService repository) {

                LoginRepository login_repository = repository.getRepository(LoginRepository.class);

                UserRepository user_repository = repository.getRepository(UserRepository.class);

                //保存 相对应的 信息
                //并且 ， 我们 要思考一下 ， 倘若 遇上 相对应的事务问题 ， 我们 应该 怎么 去 处理 
                try {
                    //目前 暂时 使用 这样的 方法 ， 倘若 相对应的 值 为 boolean 
                    //，  那么 返回值 为 Boolean.FALSE
                    login_repository.save(login);

                    user_repository.save(user);
                } catch (Exception e) {
                    return Boolean.FALSE;
                }

                return Boolean.TRUE;
            }
        });

        return user;
    }
}
