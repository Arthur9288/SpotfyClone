/**
 * useApi — hook genérico para chamadas à API com estado de loading/error.
 *
 * Uso:
 *   const { data, loading, error } = useApi(() => songsApi.getAll(), []);
 */
import { useState, useEffect } from "react";

const useApi = (fetchFn, deps = []) => {
  const [data, setData]       = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError]     = useState(null);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    setError(null);

    fetchFn()
      .then((res) => { if (!cancelled) setData(res); })
      .catch((err) => { if (!cancelled) setError(err); })
      .finally(() => { if (!cancelled) setLoading(false); });

    // Cleanup: evita setState em componente desmontado
    return () => { cancelled = true; };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, deps);

  return { data, loading, error };
};

export default useApi;
