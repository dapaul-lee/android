package com.dp.test.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by dapaul on 2017/4/4.
 */


@Entity(indexes = {
        @Index(value = "text DESC", unique = true)
})
public class DpNote {
    @Id
    private Long id;

    @NotNull
    private String text;

@Generated(hash = 465887906)
public DpNote(Long id, @NotNull String text) {
    this.id = id;
    this.text = text;
}

@Generated(hash = 231194833)
public DpNote() {
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public String getText() {
    return this.text;
}

public void setText(String text) {
    this.text = text;
}
}

