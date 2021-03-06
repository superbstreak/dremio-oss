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
package com.dremio.exec.store;

import java.util.concurrent.TimeUnit;

import com.dremio.exec.store.StoragePlugin2.UpdateStatus;
import com.dremio.service.Service;
import com.dremio.service.namespace.NamespaceException;
import com.dremio.service.namespace.NamespaceKey;
import com.dremio.service.namespace.NamespaceService;
import com.dremio.service.namespace.dataset.proto.DatasetConfig;
import com.dremio.service.namespace.source.proto.MetadataPolicy;
import com.dremio.service.namespace.source.proto.UpdateMode;
import com.google.common.base.Function;

/**
 * Manages metadata for sources and datasets under these sources.
 */
public interface CatalogService extends AutoCloseable, Service {

  MetadataPolicy REFRESH_EVERYTHING_NOW = new MetadataPolicy()
      .setAuthTtlMs(0l)
      .setDatasetUpdateMode(UpdateMode.PREFETCH)
      .setDatasetDefinitionTtlMs(0l)
      .setNamesRefreshMs(0l);
  long DEFAULT_TTL_MILLIS = TimeUnit.MILLISECONDS.convert(30, TimeUnit.MINUTES);

  MetadataPolicy DEFAULT_METADATA_POLICY = new MetadataPolicy()
      .setAuthTtlMs(DEFAULT_TTL_MILLIS)
      .setDatasetUpdateMode(UpdateMode.PREFETCH_QUERIED)
      .setDatasetDefinitionTtlMs(DEFAULT_TTL_MILLIS)
      .setNamesRefreshMs(DEFAULT_TTL_MILLIS);

  /**
   * Register a source to catalog service
   * @param source source name
   * @param sourceRegistry source registry for given source
   */
  void registerSource(NamespaceKey source, StoragePlugin2 sourceRegistry);

  /**
   * Refresh metadata cached for given source
   * @param source source name
   * @return true if cached metadata has changed
   * @throws NamespaceException
   */
  boolean refreshSource(NamespaceKey source, MetadataPolicy metadataPolicy) throws NamespaceException;

  /**
   * Create a new dataset at this location and mutate the dataset before saving.
   * @param key
   * @param datasetMutator
   */
  void createDataset(NamespaceKey key, Function<DatasetConfig, DatasetConfig> datasetMutator);

  UpdateStatus refreshDataset(NamespaceKey key) throws NamespaceException;

  /**
   * get storage plugin2 for a given source
   * @param name source name
   * @return new storage plugin, null if source does not support new storage plugin.
   */
  StoragePlugin2 getStoragePlugin(String name);

  /**
   * Create or update a physical dataset along with its read definitions and splits.
   * @param userNamespaceService namespace service for a user who is adding or modifying a dataset.
   * @param source source where dataset is to be created/updated
   * @param datasetPath dataset full path
   * @param datasetConfig minimum configuration needed to define a dataset (format settings)
   * @return true if dataset is created/updated
   * @throws NamespaceException
   */
  boolean createOrUpdateDataset(NamespaceService userNamespaceService, NamespaceKey source, NamespaceKey datasetPath, DatasetConfig datasetConfig) throws NamespaceException;

  @Deprecated
  StoragePluginRegistry getOldRegistry();
}
