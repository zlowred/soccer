package com.maxmatveev.soccer.service;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by Max Matveev on 09/06/15.
 */
public interface DataLoader {
    void loadData(Reader reader) throws DataLoaderException, IOException;
}
