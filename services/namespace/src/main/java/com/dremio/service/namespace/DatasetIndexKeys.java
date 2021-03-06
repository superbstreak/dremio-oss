/*
 * Copyright (C) 2017 Dremio Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dremio.service.namespace;

import com.dremio.datastore.SearchTypes.SearchFieldSorting;
import com.dremio.datastore.indexed.FilterIndexMapping;
import com.dremio.datastore.indexed.IndexKey;

/**
 * Index keys for dataset.
 */
public interface DatasetIndexKeys {
  IndexKey DATASET_ID = new IndexKey("id", "DATASET_ID", String.class, SearchFieldSorting.FieldType.STRING, true, false);
  IndexKey DATASET_SQL = new IndexKey("sql", "SQL", String.class, null, true, false);
  IndexKey DATASET_PARENTS = new IndexKey("par", "PARENTS", String.class, null, false, false); // to get immidiate children
  IndexKey DATASET_COLUMNS_NAMES = new IndexKey("col", "COLUMNS", String.class, null, true, false);
  IndexKey DATASET_OWNER = new IndexKey("usr", "OWNER", String.class, null, true, false);
  IndexKey DATASET_SOURCES = new IndexKey("src", "SOURCE", String.class, null, false, false);
  IndexKey DATASET_ALLPARENTS = new IndexKey("apar", "ALL_PARENTS", String.class, null, false, false); // get all descendants

  // TODO add Physical dataset search index keys

  FilterIndexMapping MAPPING = new FilterIndexMapping(DATASET_ID, DATASET_SQL, DATASET_PARENTS, DATASET_COLUMNS_NAMES,
    DATASET_OWNER, DATASET_SOURCES, DATASET_ALLPARENTS);

}
