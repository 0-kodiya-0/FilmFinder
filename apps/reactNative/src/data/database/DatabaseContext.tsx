import React, { createContext, useContext, useEffect, useState } from 'react';
import DatabaseService from './DatabaseService';

interface DatabaseContextType {
  isInitialized: boolean;
  error: string | null;
}

const DatabaseContext = createContext<DatabaseContextType>({
  isInitialized: false,
  error: null,
});

export const useDatabaseContext = () => useContext(DatabaseContext);

interface DatabaseProviderProps {
  children: React.ReactNode;
}

export const DatabaseProvider: React.FC<DatabaseProviderProps> = ({ children }) => {
  const [isInitialized, setIsInitialized] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const initDb = async () => {
      try {
        await DatabaseService.initializeDatabase();
        setIsInitialized(true);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Database initialization failed');
      }
    };

    initDb();
  }, []);

  return (
    <DatabaseContext.Provider value={{ isInitialized, error }}>
      {children}
    </DatabaseContext.Provider>
  );
};