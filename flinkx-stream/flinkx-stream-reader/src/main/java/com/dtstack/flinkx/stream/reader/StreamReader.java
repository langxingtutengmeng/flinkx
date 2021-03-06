/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dtstack.flinkx.stream.reader;

import com.dtstack.flinkx.config.DataTransferConfig;
import com.dtstack.flinkx.config.ReaderConfig;
import com.dtstack.flinkx.reader.DataReader;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.types.Row;

import java.util.List;
import java.util.Map;

/**
 * @Company: www.dtstack.com
 * @author jiangbo
 */
public class StreamReader extends DataReader {

    private long sliceRecordCount;

    private List<Map<String,Object>> columns;

    /** -1 means no limit */
    private static final long DEFAULT_SLICE_RECORD_COUNT = -1;

    public StreamReader(DataTransferConfig config, StreamExecutionEnvironment env) {
        super(config, env);

        ReaderConfig readerConfig = config.getJob().getContent().get(0).getReader();
        sliceRecordCount = readerConfig.getParameter().getLongVal("sliceRecordCount",DEFAULT_SLICE_RECORD_COUNT);
        columns = readerConfig.getParameter().getColumn();
    }

    @Override
    public DataStream<Row> readData() {
        StreamInputFormatBuilder builder = new StreamInputFormatBuilder();
        builder.setColumns(columns);
        builder.setSliceRecordCount(sliceRecordCount);
        builder.setMonitorUrls(monitorUrls);
        builder.setBytes(bytes);
        return createInput(builder.finish(),"streamreader");
    }
}
