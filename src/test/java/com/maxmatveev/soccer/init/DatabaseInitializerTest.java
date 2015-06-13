package com.maxmatveev.soccer.init;

import com.maxmatveev.soccer.service.DataLoader;
import com.maxmatveev.soccer.service.DataLoaderException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

import static org.mockito.Mockito.*;

public class DatabaseInitializerTest {
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private NamedParameterJdbcOperations jdbc;
    @Mock
    private DataLoader csvDataLoader;
    private DatabaseInitializer databaseInitializer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        databaseInitializer = new DatabaseInitializer();
        databaseInitializer.setApplicationContext(applicationContext);
        databaseInitializer.setCsvDataLoader(csvDataLoader);
        databaseInitializer.setJdbc(jdbc);
    }

    @Test
    public void testExistingDatabaseNotOverwriten() {
        when(jdbc.queryForObject(eq("select COUNT(*) from INFORMATION_SCHEMA.SYSTEM_TABLES Where TABLE_NAME in ('Teams', 'Players', 'Leagues')"), any(Map.class), eq(Integer.class))).thenReturn(3);
        databaseInitializer.init();
        verify(jdbc).queryForObject(eq("select COUNT(*) from INFORMATION_SCHEMA.SYSTEM_TABLES Where TABLE_NAME in ('Teams', 'Players', 'Leagues')"), any(Map.class), eq(Integer.class));
        verifyNoMoreInteractions(jdbc);
    }

    @Test
    public void testTablesCreated() throws IOException {
        when(jdbc.queryForObject(eq("select COUNT(*) from INFORMATION_SCHEMA.SYSTEM_TABLES Where TABLE_NAME in ('Teams', 'Players', 'Leagues')"), any(Map.class), eq(Integer.class))).thenReturn(0);
        Resource mockResource = Mockito.mock(Resource.class);
        when(applicationContext.getResource(anyString())).thenReturn(mockResource);
        InputStream mockInputStream = Mockito.mock(InputStream.class);
        when(mockResource.getInputStream()).thenReturn(mockInputStream);
        databaseInitializer.init();
        verify(jdbc).queryForObject(eq("select COUNT(*) from INFORMATION_SCHEMA.SYSTEM_TABLES Where TABLE_NAME in ('Teams', 'Players', 'Leagues')"), any(Map.class), eq(Integer.class));
        verify(jdbc, times(6)).update(anyString(), any(Map.class));
        verifyNoMoreInteractions(jdbc);
    }

    @Test
    public void testImportGotCalled() throws IOException, DataLoaderException {
        when(jdbc.queryForObject(eq("select COUNT(*) from INFORMATION_SCHEMA.SYSTEM_TABLES Where TABLE_NAME in ('Teams', 'Players', 'Leagues')"), any(Map.class), eq(Integer.class))).thenReturn(0);
        Resource mockResource = Mockito.mock(Resource.class);
        when(applicationContext.getResource(anyString())).thenReturn(mockResource);
        InputStream mockInputStream = Mockito.mock(InputStream.class);
        when(mockResource.getInputStream()).thenReturn(mockInputStream);
        databaseInitializer.init();
        verify(csvDataLoader).loadData(Matchers.<Reader>any());
    }

    @Test(expected = IllegalStateException.class)
    public void testLoaderExceptionHandled() throws IOException, DataLoaderException {
        when(jdbc.queryForObject(eq("select COUNT(*) from INFORMATION_SCHEMA.SYSTEM_TABLES Where TABLE_NAME in ('Teams', 'Players', 'Leagues')"), any(Map.class), eq(Integer.class))).thenReturn(0);
        Resource mockResource = Mockito.mock(Resource.class);
        when(applicationContext.getResource(anyString())).thenReturn(mockResource);
        InputStream mockInputStream = Mockito.mock(InputStream.class);
        when(mockResource.getInputStream()).thenReturn(mockInputStream);
        doThrow(new DataLoaderException("Test")).when(csvDataLoader).loadData(Matchers.<Reader>any());
        databaseInitializer.init();
    }
}