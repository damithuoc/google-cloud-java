/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.bigquery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.cloud.bigquery.TimePartitioning.Type;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TimePartitioningTest {

  private static final Type TYPE = Type.DAY;
  private static final long EXPIRATION_MS = 42;
  private static final TimePartitioning TIME_PARTITIONING =
      TimePartitioning.of(TYPE, EXPIRATION_MS);

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testOf() {
    assertEquals(TYPE, TIME_PARTITIONING.getType());
    assertEquals(EXPIRATION_MS, TIME_PARTITIONING.getExpirationMs().longValue());
    TimePartitioning partitioning = TimePartitioning.of(TYPE);
    assertEquals(TYPE, partitioning.getType());
    assertNull(partitioning.getExpirationMs());
  }

  @Test
  public void testOfDeprecated() {
    assertEquals(TYPE, TIME_PARTITIONING.type());
    assertEquals(EXPIRATION_MS, TIME_PARTITIONING.expirationMs().longValue());
    TimePartitioning partitioning = TimePartitioning.of(TYPE);
    assertEquals(TYPE, partitioning.type());
    assertNull(partitioning.expirationMs());
  }

  @Test
  public void testTypeOf_Npe() {
    thrown.expect(NullPointerException.class);
    TimePartitioning.of(null);
  }

  @Test
  public void testTypeAndExpirationOf_Npe() {
    thrown.expect(NullPointerException.class);
    TimePartitioning.of(null, EXPIRATION_MS);
  }

  @Test
  public void testToAndFromPb() {
    compareTimePartitioning(TIME_PARTITIONING, TimePartitioning.fromPb(TIME_PARTITIONING.toPb()));
    TimePartitioning partitioning = TimePartitioning.of(TYPE);
    compareTimePartitioning(partitioning, TimePartitioning.fromPb(partitioning.toPb()));
  }

  private void compareTimePartitioning(TimePartitioning expected, TimePartitioning value) {
    assertEquals(expected, value);
    assertEquals(expected.getType(), value.getType());
    assertEquals(expected.getExpirationMs(), value.getExpirationMs());
    assertEquals(expected.hashCode(), value.hashCode());
    assertEquals(expected.toString(), value.toString());
  }
}
